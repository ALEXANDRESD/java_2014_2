
/*
 * Implementaусo do Bloqueto de Cobranуas do Banco do Brasil
 * - ConvЖnio com 1000000
 * 
 * scoelho@stefanini.com
 */
package br.com.stefanini.treinamento.boleto;

import java.math.BigDecimal;
import java.util.Date;

import br.com.stefanini.treinamento.exception.ManagerException;

public class BloquetoBBConvenioAcima1000000 extends BloquetoBBImpl implements
		BloquetoBB {

	@Override
	protected void validaDados() throws ManagerException {

		if (codigoBanco == null || codigoBanco.length() != 3) {
			throw new ManagerException(
					"Cзdigo do Banco nсo informado ou com tamanho diferente de 3 posiушes");
		}

		if (codigoMoeda == null || codigoMoeda.length() != 1) {
			throw new ManagerException(
					"Cзdigo de moeda nсo informado ou invрlido");
		}

		if (dataVencimento == null) {
			throw new ManagerException("Data de vencimento nсo informada");
		}

		if (valor == null) {
			throw new ManagerException(
					"Valor do bloqueto banc├Аrio nсo informado");
		}

		if (numeroConvenioBanco == null || numeroConvenioBanco.length() != 7) {
			throw new ManagerException(
					"nЩmero de convЖnio nсo informado ou o convЖnio informado ж invрlido. O convЖnio deve ter 4 posiушes");
		}

		if (complementoNumeroConvenioBancoSemDV == null
				&& complementoNumeroConvenioBancoSemDV.length() != 10) {
			throw new ManagerException(
					"Complemento do nЩmero do convЖnio nсo informado. O complemento deve ter 7 posiушes");
		}

		if (tipoCarteira == null || tipoCarteira.length() != 2) {
			throw new ManagerException(
					"Tipo carteira nсo informado ou o valor ж invрlido");
		}

		if (dataBase == null) {
			throw new ManagerException("A database nсo foi informada.");
		}

	}

	public BloquetoBBConvenioAcima1000000(String codigoBanco,
			String codigoMoeda, Date dataVencimento, Date dataBase,
			BigDecimal valor, String numeroConvenioBanco,
			String complementoNumeroConvenioBancoSemDV,
			String numeroAgenciaRelacionamento,
			String contaCorrenteRelacionamentoSemDV, String tipoCarteira)
			throws ManagerException {

		this.codigoBanco = codigoBanco;
		this.codigoMoeda = codigoMoeda;
		this.dataVencimento = dataVencimento;
		this.valor = valor;
		this.numeroConvenioBanco = numeroConvenioBanco;
		
		this.complementoNumeroConvenioBancoSemDV = complementoNumeroConvenioBancoSemDV;
		this.numeroAgenciaRelacionamento = numeroAgenciaRelacionamento;
		this.contaCorrenteRelacionamentoSemDV = contaCorrenteRelacionamentoSemDV;
		this.tipoCarteira = tipoCarteira;
		this.dataBase = dataBase;

		validaDados();

	}

	@Override
	protected String getLDNumeroConvenio() {

		String convenio = String.format("%07d",
				Long.valueOf(numeroConvenioBanco));
		return String.format("%s.%s", convenio.substring(0, 1),
				convenio.substring(1, 5));

	}

	// TODO: @sandro - refatorar os m├Еtodos getCodigoBarrasSemDigito() e
	// getCodigoBarras()
	@Override
	protected String getCodigoBarrasSemDigito() {

		init();

		StringBuilder buffer = new StringBuilder();

		buffer.append(codigoBanco);
		buffer.append(codigoMoeda);
		buffer.append(fatorVencimento);
		buffer.append(getValorFormatado());
		buffer.append("000000"); // ou String.format("%06d",0);
		buffer.append(numeroConvenioBanco);
		buffer.append(complementoNumeroConvenioBancoSemDV);
		buffer.append(tipoCarteira);

		return buffer.toString();
	}

	@Override
	public String getCodigoBarras() {

		init();

		StringBuilder buffer = new StringBuilder();

		buffer.append(codigoBanco); // campo 01-03(03)
		buffer.append(codigoMoeda); // campo 04-04 (01)
		buffer.append(digitoVerificadorCodigoBarras(getCodigoBarrasSemDigito())); // campo
																					// 05-05(1)

		buffer.append(fatorVencimento); // Campo 06-09(04)
		buffer.append(getValorFormatado()); // Campo 10-19 (10)
		buffer.append("000000");  // ou String.format("%06d",0);
		

		buffer.append(numeroConvenioBanco); // //Campo 26-32 (5)
		buffer.append(complementoNumeroConvenioBancoSemDV); // Campo 33-42 (10)
		buffer.append(tipoCarteira); // Campo 43-44(02)

		return buffer.toString();
	}

	
	
}
