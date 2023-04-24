package com.bit.bitcare.dao;

import com.bit.bitcare.model.AddressDTO;
import org.springframework.stereotype.Component;

@Component
public interface AddressDAO {
    public void insert(AddressDTO addressDTO);
    public AddressDTO select(int id);
}
