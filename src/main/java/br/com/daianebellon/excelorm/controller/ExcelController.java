package br.com.daianebellon.excelorm.controller;

import br.com.daianebellon.excelorm.lib.ExcelConverter;
import br.com.daianebellon.excelorm.model.Pessoa;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/excel")
@RestController
public class ExcelController {

    @GetMapping
    public List<Pessoa> convert() throws Exception {
        return new ExcelConverter().convert("/home/felix.gilioli/Documents/planilha.xlsx", Pessoa.class);
    }
}
