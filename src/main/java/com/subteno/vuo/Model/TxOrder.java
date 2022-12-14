package com.subteno.vuo.Model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TxOrder {
    String UID;
    String name;
    String emailAddress;
    String address;
    String timestamp;
    List<Item> item;
}
