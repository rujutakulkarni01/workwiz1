package com.example.myapplication;

public class Users{
    private String name,email,phoneNo,password,documentId;

    public Users(String name, String email, String phoneNo,String password,String documentId)
    {

        this.name = name;
        this.phoneNo = phoneNo;
        this.email = email;
        this.documentId = documentId;
    }

    public Users(){


    }

    public String getName(){
        return name;
    }

    public void setName(String name){

        this.name = name;
    }

    public String getDocumentId(){
        return documentId;
    }

    public void setDocumentId(String documentId){

        this.documentId = documentId;
    }

    public String getPhoneNo(){
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo){

        this.phoneNo = phoneNo;
    }
    public String getEmail(){
        return email;
    }

    public void setEmail(String email){

        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){

        this.password = password;
    }
}
