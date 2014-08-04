

/*
 * Implementaусo do Bloqueto de Cobranуas do Banco do Brasil
 * - ConvЖnio com 4 posiушes 
 * 
 * scoelho@stefanini.com
 */
package br.com.stefanini.treinamento.boleto;

import java.math.BigDecimal;
import java.util.Date;

import br.com.stefanini.treinamento.exception.ManagerException;

public class BloquetoBBConvenio4 extends BloquetoBBImpl implements BloquetoBB {

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

		if (numeroConvenioBanco == null || numeroConvenioBanco.length() != 4) {
			throw new ManagerException(
					"nЩmero de convЖnio nсo informado ou o convЖnio informado ж invрlido. O convЖnio deve ter 4 posiушes");
		}

		if (complementoNumeroConvenioBancoSemDV == null
				&& complementoNumeroConvenioBancoSemDV.length() != 7) {
			throw new ManagerException(
					"Complemento do nЩmero do convЖnio nсo informado. O complemento deve ter 7 posiушes");
		}

		if (numeroAgenciaRelacionamento == null
				|| numeroAgenciaRelacionamento.length() != 4) {
			throw new ManagerException(
					"nЩmero da agЖncia de Relacionamento nсo informado. O nЩmero da agЖncia deve ter 4 posiушes");
		}

		if (contaCorrenteRelacionamentoSemDV == null
				|| contaCorrenteRelacionamentoSemDV.length() != 8) {
			throw new ManagerException(
					"Conta corrente de relacionamento nсo informada. O nЩmero da conta deve ter 8 posiушes");
		}

		if (tipoCarteira == null || tipoCarteira.length() != 2) {
			throw new ManagerException(
					"Tipo carteira nсo informado ou o valor ж invрlido");
		}

		if (dataBase == null) {
			throw new ManagerException("A database nсo foi informada.");
		}

	}

	public BloquetoBBConvenio4(String codigoBanco, String codigoMoeda,
			Date dataVencimento, Date dataBase, BigDecimal valor,
			String numeroConvenioBanco,
			String complementoNumeroConvenioBancoSemDV,
			String numeroAgenciaRelacionamento,
			String contaCorrenteRelacionamentoSemDV, String tipoCarteira)
			throws ManagerException {

		validaDados(); 

	}

	@Override
	protected String getLDNumeroConvenio() {
		String convenio = String.format("%04d",
				Long.valueOf(numeroConvenioBanco));
		return String.format("%s.%s", convenio.substring(0, 1),
				convenio.substring(1, 5));

	}

	// TODO: @sandro - refatorar os mжtodos getCodigoBarrasSemDigito() e
	// getCodigoBarras()



	@Override
	protected String getCodigoBarrasSemDigito() {
		// junta as string para formar o codigo de barras sem o digьto
		init();

		StringBuilder buffer = new StringBuilder();
		
		buffer.append(codigoBanco);
		buffer.append(codigoMoeda);
		buffer.append(fatorVencimento);
		buffer.append(getValorFormatado());
		buffer.append(numeroConvenioBanco);
		buffer.append(complementoNumeroConvenioBancoSemDV);
		buffer.append(numeroAgenciaRelacionamento);
		buffer.append(contaCorrenteRelacionamentoSemDV);
		buffer.append(tipoCarteira);

		return buffer.toString();
	}

	@Override
	public String getCodigoBarras() {

		init();

		StringBuilder buffer = new StringBuilder();

		buffer.append(codigoBanco); // campo 01-03(03)
		buffer.append(codigoMoeda); // campo 04-04 (01)
		buffer.append(digitoVerificadorCodigoBarras(getCodigoBarrasSemDigito()));
		
		buffer.append(fatorVencimento);  //Campo 06-09(04)
		buffer.append(getValorFormatado());  //Campo 10-19 (10)
		buffer.append(numeroConvenioBanco);  //Campo 20-23 (04)
		
		buffer.append(complementoNumeroConvenioBancoSemDV);   //Campo  24-30 (7)
		buffer.append(numeroAgenciaRelacionamento);  //Campo 31-34(4) 
		buffer.append(contaCorrenteRelacionamentoSemDV);  //Campo 35-42(8)
		buffer.append(tipoCarteira);  //Campo 43-44(02)
		
		return buffer.toString();
	}

}

