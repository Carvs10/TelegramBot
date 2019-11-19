package br.ufrn.imd.lpii.telegram.bot;

/**
 *
 */
class Localizacao
{
	String Nome;
	String Descricao;

	Localizacao(String Nome, String Descricao){
		this.Nome 		= Nome;
		this.Descricao = Descricao;
	}

	// Acessores
	String getNome() {
		return this.Nome;
	}
	String getDescricao() {
		return this.Descricao;
	}
	void setNome(String Nome) {
		this.Nome = Nome;
	}
	void setDescricao(String Descricao) {
		this.Descricao = Descricao;
	}
}
