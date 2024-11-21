package e_com.bichitra.e_com02092024.service;

import e_com.bichitra.e_com02092024.model.Users;
import e_com.bichitra.e_com02092024.payload.AddressDTO;

import java.util.List;

public interface AddressService {

    AddressDTO createAddress(AddressDTO addressDTO, Users users);

    List<AddressDTO> getAllAddress();

    AddressDTO getAddressById(Long addressId);


    List<AddressDTO> getAddressByUser(Long user_id);

    AddressDTO updateAddressById(Long addressId,AddressDTO addressDTO);

    AddressDTO partialUpdateAddressById(Long addressId, AddressDTO addressDTO);

    String deleteAddress(Long addressId);
}
