package com.compdes.storedFiles.models.entities;

import org.hibernate.annotations.DynamicUpdate;

import com.compdes.common.models.entities.Auditor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa los metadatos de un archivo almacenado.
 *
 * Esta clase contiene la información necesaria para identificar y gestionar
 * archivos físicos en el sistema.
 * 
 * @author Luis Monterroso
 * @version 1.0
 * @since 2025-06-02
 */
@Entity
@DynamicUpdate
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class StoredFile extends Auditor {

    @Column(nullable = false, unique = true, length = 200)
    private String fileName;

    @Column(nullable = false, length = 20)
    private String extension;

    @Column(nullable = false, length = 200)
    private String mimeType;

    /**
     * Constructor utilizado para crear una nueva entidad {@link StoredFile} con sus
     * atributos esenciales.
     *
     * Este constructor se utiliza principalmente para persistir una nueva instancia
     * del archivo
     * con nombre interno, extensión y tipo MIME definidos antes de ser almacenado
     * en base de datos.
     *
     * @param fileName  nombre único del archivo sin extensión (por ejemplo, un
     *                  UUID)
     * @param extension extensión del archivo (por ejemplo, "pdf", "png")
     * @param mimeType  tipo MIME correspondiente al contenido del archivo (por
     *                  ejemplo, "application/pdf")
     */
    public StoredFile(String fileName, String extension, String mimeType) {
        this.fileName = fileName;
        this.extension = extension;
        this.mimeType = mimeType;
    }

    /**
     * Retorna el nombre completo del archivo, compuesto por su nombre base y
     * extensión.
     *
     * Este valor representa cómo se guarda físicamente el archivo en el sistema de
     * archivos.
     * Ejemplo: si {@code fileName = "abc123"} y {@code extension = "png"}, retorna
     * {@code "abc123.png"}.
     *
     * @return nombre completo del archivo incluyendo la extensión
     */
    public String getFullName() {
        return fileName + "." + extension;
    }

}
