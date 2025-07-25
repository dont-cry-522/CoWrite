package com.cowrite.project.model.entity;


import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "doc_management")
public class DocMongodb {

    public String id;
}
;