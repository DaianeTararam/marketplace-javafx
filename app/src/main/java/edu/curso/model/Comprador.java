package edu.curso.model;

public class Comprador extends Usuario{
    private Endereco endereco;
    private Telefone telefone;
    public Endereco getEndereco() {
        return endereco;
    }
    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
    public Telefone getTelefone() {
        return telefone;
    }   

    public void setTelefone(Telefone telefone) {
        this.telefone = telefone;
    }


}
