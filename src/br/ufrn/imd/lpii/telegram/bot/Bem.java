package br.ufrn.imd.lpii.telegram.bot;

public class Bem
{
	private String Nome;
	private String Descricao;
	private String codigo;
	private String localizacao;
	private String categoria;

	Bem(){ /**/	}

	Bem(String Nome , String Descricao , String codigo, String localizacao, String categoria) {
		this.Nome      		= Nome;
		this.Descricao 		= Descricao;
		this.codigo    		= codigo;
		this.localizacao 	= localizacao;
		this.categoria 		= categoria;
	}

	// Acessores
	String getNome() {
		return this.Nome;
	}
	String getDescricao() {
		return this.Descricao;
	}
	String getCodigo() {
		return this.codigo;
	}
	String getLocalizacao() { return this.localizacao; }
	String getCategoria() { return this.categoria; }

	void setNome(String Nome) { this.Nome = Nome; }
	void setDescricao(String Descricao) {
		this.Descricao = Descricao;
	}
	void setCodigo(String codigo) { this.codigo = codigo; }
	void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
	void setCategoria(String categoria) {
		this.categoria = categoria;
	}
}
