package e_com.bichitra.e_com02092024.controller;

import e_com.bichitra.e_com02092024.model.Users;
import e_com.bichitra.e_com02092024.payload.AddressDTO;
import e_com.bichitra.e_com02092024.service.AddressService;
import e_com.bichitra.e_com02092024.utils.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AddressController {
    @Autowired
    private AddressService addressService;
    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@RequestBody @Valid AddressDTO addressDTO){
        final Users users = authUtil.loggedInUsername();
        final AddressDTO addressDTOFromService = addressService.createAddress(addressDTO, users);
        return new ResponseEntity<>(addressDTOFromService, HttpStatus.CREATED);
    }
    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddress(){
        final List<AddressDTO> addressDTOList = addressService.getAllAddress();
        return ResponseEntity.ok(addressDTOList);
    }
    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressByID(@PathVariable Long addressId){
        final AddressDTO addressDTO = addressService.getAddressById(addressId);
        return ResponseEntity.ok(addressDTO);
    }
    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getAddressByUser(){
        final Long userId = authUtil.loggedInUserId();
        final List<AddressDTO> addressDTOList = addressService.getAddressByUser(userId);
        return ResponseEntity.ok(addressDTOList);
    }
    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddressById(@PathVariable Long addressId
            ,@RequestBody AddressDTO addressDTO){
        final var updatedAddress = addressService.updateAddressById(addressId, addressDTO);
        return ResponseEntity.ok(updatedAddress);
    }
    @PatchMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> partialUpdateAddressById(@PathVariable Long addressId
            ,@RequestBody AddressDTO addressDTO){
        final AddressDTO addressDTOFromService = addressService.partialUpdateAddressById(addressId, addressDTO);
        return ResponseEntity.ok().body(addressDTOFromService);
    }
    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> updateAddress(@PathVariable Long addressId){
        final String status = addressService.deleteAddress(addressId);
        return  ResponseEntity.ok(status);
    }

}
