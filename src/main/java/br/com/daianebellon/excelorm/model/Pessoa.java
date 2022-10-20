package br.com.daianebellon.excelorm.model;

import br.com.daianebellon.excelorm.lib.annotations.ExcelColumn;
import br.com.daianebellon.excelorm.lib.annotations.ExcelTable;

@ExcelTable
public class Pessoa {

    @ExcelColumn(position = 0)
    private Integer id;

    @ExcelColumn(position = 1)
    private String nome;

    @ExcelColumn(position = 2)
    private Integer idade;

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }
}
