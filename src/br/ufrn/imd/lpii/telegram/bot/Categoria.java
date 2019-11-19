package br.ufrn.imd.lpii.telegram.bot;

public class Categoria
{
	String Nome;
	String Descricao;
	String Codigo;

	Categoria(String Nome, String Descricao, String Codigo) {
		this.Nome      = Nome;
		this.Descricao = Descricao;
		this.Codigo    = Codigo;
	}

	String getNome() {
		return this.Nome;
	}
	String getDescricao() {
		return this.Descricao;
	}
	String getCodigo() {
		return this.Codigo;
	}
	void setNome(String Nome) {
		this.Nome = Nome;
	}
	void setDescricao(String Descricao) {
		this.Descricao = Descricao;
	}
	void setCodigo(String Codigo) {
		this.Codigo = Codigo;
	}
}
