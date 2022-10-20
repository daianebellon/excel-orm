package br.com.daianebellon.excelorm.lib;

import br.com.daianebellon.excelorm.lib.annotations.ExcelColumn;
import br.com.daianebellon.excelorm.lib.annotations.ExcelTable;
import br.com.daianebellon.excelorm.lib.exceptions.InvalidRepresentationException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelConverter {

    public <T> List<T> convert(String pathFile, Class<T> clazz) throws Exception {
        if (!clazz.isAnnotationPresent(ExcelTable.class)) {
            throw new InvalidRepresentationException("Classe " + clazz.getName() + " não é uma representação de ExcelTable.");
        }

        Field[] atributosDaClasse = clazz.getDeclaredFields();
        Method[] metodosDaClasse = clazz.getDeclaredMethods();

        Workbook planilha = new XSSFWorkbook(new FileInputStream(pathFile));
        Sheet abaTabela = planilha.getSheetAt(clazz.getAnnotation(ExcelTable.class).tab());

        Iterator<Row> rowIterator = abaTabela.iterator();
        rowIterator.next();

        List<T> registrosDaTabela = new ArrayList<>();

        while (rowIterator.hasNext()) {
            Row currentRow = rowIterator.next();

            T registro = clazz.getConstructor().newInstance();


            Iterator<Cell> iterator = currentRow.iterator();
            for (int i = 0; iterator.hasNext(); i++) {
                Cell currentCell = iterator.next();

                Field colunaAtual = getAtributoByPosition(atributosDaClasse, i);

                String nomeDoCampo = colunaAtual.getName();
                Method setter = getSetter(metodosDaClasse, nomeDoCampo);

                if (colunaAtual.getType().equals(String.class)) {
                    String valorDoCampo = currentCell.getStringCellValue();
                    setter.invoke(registro, valorDoCampo);
                } else if (colunaAtual.getType().equals(Integer.class)) {
                    int valorDoCampo = (int) currentCell.getNumericCellValue();
                    setter.invoke(registro, valorDoCampo);
                }

            }

            registrosDaTabela.add(registro);
        }

        return registrosDaTabela;
    }

    private Method getSetter(Method[] metodosDaClasse, String nomeDoCampo) {
        String nomeDoSetter = "set" + StringUtils.capitalize(nomeDoCampo);
        for (Method method : metodosDaClasse) {

            if (method.getName().equals(nomeDoSetter)) {
                return method;
            }
        }

        return null;
    }

    private Field getAtributoByPosition(Field[] atributosDaClasse, int position) {
        for (Field field : atributosDaClasse) {
            if (field.getAnnotation(ExcelColumn.class).position() == position) {
                return field;
            }
        }

        return null;
    }

}
