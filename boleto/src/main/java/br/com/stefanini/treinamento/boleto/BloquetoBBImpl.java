package br.com.stefanini.treinamento.boleto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import br.com.stefanini.treinamento.exception.ManagerException;

public abstract class BloquetoBBImpl implements BloquetoBB {

	protected String codigoBanco;
	protected String codigoMoeda;
	protected String fatorVencimento;
	protected Date dataVencimento;
	protected Date dataBase;
	protected BigDecimal valor;
	protected String numeroConvenioBanco;
	protected String complementoNumeroConvenioBancoSemDV;
	protected String numeroAgenciaRelacionamento;
	protected String contaCorrenteRelacionamentoSemDV;
	protected String tipoCarteira;

	private int dvCodigoBarras;

	protected abstract void validaDados() throws ManagerException;

	/**
	 * Inicializa o fator de vencimento
	 */
	protected void setFatorVencimento() {

		long dias = diferencaEmDias(dataBase, dataVencimento);

		// TODO: EXPLICAR O QUE ESTE MЙTODO ESTБ FAZENDO
		/*
		 * o mйtodo seta a variavel fator fatorVencimento convertendo o valor da
		 * variavel dias que esta em ms passados em um valor em nъmero inteiro
		 * de dias entre duas datas a variael dias recebedo do metodo
		 * diferencaEmDias a diferenзa entre duas datas
		 */

		fatorVencimento = String.format("%04d", dias);

	}

	/**
	 * Inicializa os valores, formata
	 */
	protected void init() {

		setFatorVencimento();

	}

	/**
	 * Retorna o valor formatado do boleto bancбrio
	 * 
	 * @return
	 */
	protected String getValorFormatado() {

		// TODO: Explicar o que este mйtodo estб fazendo
		/*
		 * O metуdo transforma de um valor em moeda, para um valor String. O
		 * valor do boleto й adicionado e atravйs do metуdo
		 * " valor.setScale(2, RoundingMode.HALF_UP)" ele й arrendondado e
		 * transformado em um nъmeral de duas casas decimais depois da vнgula.
		 * Apуs isso o nъmero й transformado em string e onde for ponto ele
		 * substitui por vazio concatenando o nъmero
		 */
		return String.format(
				"%010d",
				Long.valueOf(valor.setScale(2, RoundingMode.HALF_UP).toString()
						.replace(".", "")));
	}

	/**
	 * Formata o nъmero do convкnio da Linha Digitбvel
	 * 
	 * @return
	 */
	protected abstract String getLDNumeroConvenio();// verificar a utilidade
													// disto

	/**
	 * Retorna o cуdigo de barras do Bloqueto
	 * 
	 * @return cуdigo de barras
	 */
	protected abstract String getCodigoBarrasSemDigito();

	public abstract String getCodigoBarras();

	/**
	 * Campo 5 da Linha Digitбvel
	 * 
	 * @return
	 */
	private String ldCampo5() {

		StringBuilder buffer = new StringBuilder();
		buffer.append(fatorVencimento); // contendo fator venc, mais o valor
		buffer.append(getValorFormatado());
		return buffer.toString();
	}

	/**
	 * Campo 4 da Linha Digitбvel
	 * 
	 * @return
	 */
	private String ldCampo4() {/*
		StringBuilder buffer = new StringBuilder();		
		//buffer.append(getDvCodigoBarras());// digito verificador do cod barras
		buffer.append(digitoVerificadorCodigoBarras(getCodigoBarrasSemDigito()));
		return buffer.toString();*/
		
		return String.valueOf(digitoVerificadorCodigoBarras(getCodigoBarrasSemDigito()));
		
		
	}

	/**
	 * Campo 3 da Linha Digitбvel
	 * 
	 * @return
	 */
	private String ldCampo3() {

		return String.format("%s.%s", getCodigoBarras().substring(34, 39),
				getCodigoBarras().substring(39, 44));
	}

	/**
	 * Campo 2 da Linha Digitбvel
	 * 
	 * @return
	 */
	private String ldCampo2() {
		return String.format("%s.%s", getCodigoBarras().substring(24, 29),
				getCodigoBarras().substring(29, 34));
	}

	/**
	 * Calcula o digito verificador do campo
	 * 
	 * @param campo
	 * @return
	 */
	protected int digitoVerificadorPorCampo(String campo, boolean valor) {
	
	


		return 0;
	}

	/**
	 * Calcula o digito verificado do cуdigo de barras
	 * 
	 * @param string
	 * 
	 * @return
	 */
	protected int digitoVerificadorCodigoBarras(String string) {
		// TODO: COMPLETAR
		return 0;
	}

	/**
	 * Campo 1 da Linha Digitбvel
	 * 
	 * - Cуdigo do Banco - Cуdigo da Moeda - Nъmero do convкnio
	 * 
	 * @return
	 */
	private String ldCampo1() {

		StringBuilder buffer = new StringBuilder();
		buffer.append(codigoBanco);
		buffer.append(codigoMoeda);
		buffer.append(getLDNumeroConvenio());// posição 20 24 do codigo de
												// barras
		return buffer.toString();

	}

	public String getLinhaDigitavel() {

		init();

		StringBuilder buffer = new StringBuilder();

		buffer.append(ldCampo1());
		buffer.append(digitoVerificadorPorCampo(ldCampo1(), true)); 
		buffer.append(" "); 
		buffer.append(ldCampo2()); 
		buffer.append(digitoVerificadorPorCampo(ldCampo2(), false)); 
		buffer.append(" "); 		
		buffer.append(ldCampo3()); 
		buffer.append(digitoVerificadorPorCampo(ldCampo3(), false)); 
		
		buffer.append(" "); 
		buffer.append(ldCampo4()); 
		buffer.append(" ");
		buffer.append(ldCampo5()); 

		return buffer.toString();
	}

	/**
	 * Retorna a diferenзa em dias de duas datas
	 * 
	 * @param dataInicial
	 *            Data inicial
	 * @param dataFinal
	 *            Data final
	 * @return
	 */
	protected static long diferencaEmDias(Date dataInicial, Date dataFinal) {

		// TODO: Estude a Math e escreva aqui o que este mйtodo estб fazendo
		/*
		 * Calcula a diferenзa entre duas datas, obtendo um resultado em
		 * segundos Fazendo a divisгo por ms obten-se um inteiro que atravйs do
		 * Math.round й arredondado para o nъmero inteiro mais proximo
		 */

		return Math
				.round((dataFinal.getTime() - dataInicial.getTime()) / 86400000D);
	}

	// verificar a utilidade desta função
	public int getDvCodigoBarras() {

		getCodigoBarras();

		return dvCodigoBarras;
	}
}
