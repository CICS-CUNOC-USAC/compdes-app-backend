package com.compdes.reports.csv.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.springframework.stereotype.Component;

import com.compdes.common.exceptions.CustomRuntimeException;
import com.compdes.common.exceptions.enums.CustomRuntimeErrorEnum;
import com.opencsv.CSVWriter;

import lombok.RequiredArgsConstructor;

/**
 * Utilidad para la generación de archivos CSV en memoria.
 * 
 * Proporciona métodos para escribir colecciones de datos en formato CSV.
 */
@Component
@RequiredArgsConstructor
public class CsvExporter {

    /**
     * Escribe todas las líneas proporcionadas en un archivo CSV en memoria.
     * 
     * Utiliza un {@link CSVWriter} para generar el contenido en formato CSV a
     * partir
     * de la lista de arreglos de cadenas. Cada arreglo representa una fila y cada
     * elemento del arreglo una celda dentro de la fila. El resultado se retorna
     * como
     * un arreglo de bytes que puede ser enviado como respuesta HTTP, almacenado o
     * procesado posteriormente.
     * 
     * @param lines lista de filas a escribir, donde cada fila es un arreglo de
     *              cadenas que representa las columnas
     * @return el contenido CSV generado en forma de arreglo de bytes
     * @throws CustomRuntimeException si ocurre un error de E/S durante la escritura
     */
    
    public byte[] writeAllLines(List<String[]> lines) {
        try (
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(baos);
                CSVWriter writer = new CSVWriter(
                        outputStreamWriter)) {
            // escribe todas las lineas en el archivo
            writer.writeAll(lines);
            writer.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            throw CustomRuntimeErrorEnum.CSV_WRITE_FAILED.getCustomRuntimeException();
        }
    }

}
