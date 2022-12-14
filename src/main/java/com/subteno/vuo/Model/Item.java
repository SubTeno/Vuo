package com.subteno.vuo.Model;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @NotEmpty(message = "name can't be empty!")
    String name;
    @NotNull(message = "price can't be empty!")
    Integer price;
    @NotEmpty(message = "description can't be empty!")
    String description;
    @Nonnull
    String link;
}
