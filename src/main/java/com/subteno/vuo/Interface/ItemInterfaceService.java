package com.subteno.vuo.Interface;

import org.springframework.web.multipart.MultipartFile;

import com.subteno.vuo.Model.Item;
import com.subteno.vuo.Model.Result;

public interface ItemInterfaceService {
    Result AddItem(Item item, MultipartFile file) throws Exception;

    Result DeleteItem(String itemName) throws Exception;

    Result GetItem(String itemName) throws Exception;

    Result GetItems(Integer offset, Integer limit) throws Exception;
}
