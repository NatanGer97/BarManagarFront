package com.example.barmanagarfront.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class Branch
{
   @NotEmpty private String branchName;
   @NotEmpty private String country;
   @NotEmpty private String city;

    public Branch(String branchName, String country, String city)
    {
        this.branchName = branchName;
        this.country = country;
        this.city = city;
    }
}
