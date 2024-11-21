package e_com.bichitra.e_com02092024.service.impl;

import e_com.bichitra.e_com02092024.exception.ResourceNotFoundException;
import e_com.bichitra.e_com02092024.model.Address;
import e_com.bichitra.e_com02092024.model.Users;
import e_com.bichitra.e_com02092024.payload.AddressDTO;
import e_com.bichitra.e_com02092024.repository.AddressRepository;
import e_com.bichitra.e_com02092024.repository.UserRepository;
import e_com.bichitra.e_com02092024.service.AddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, Users users){
        final Address address = modelMapper.map(addressDTO, Address.class);
        address.setUsers(users);
        final Address saveAddress = addressRepository.save(address);
        final List<Address> addresseList = users.getAddresses();
        addresseList.add(saveAddress);
        users.setAddresses(addresseList);
        return modelMapper.map(address,AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAllAddress(){
        final List<Address> addressList = addressRepository.findAll();
        final List<AddressDTO> addressDTOList = addressList.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class)).toList();
        return addressDTOList;
    }
    @Override
    public AddressDTO getAddressById(Long addressId){
        final Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        final AddressDTO addressDTO = modelMapper.map(address, AddressDTO.class);
        return addressDTO;
    }
    @Override
    public List<AddressDTO> getAddressByUser(Long user_id){
        final List<Address> addressList = addressRepository.findByUserId(user_id);
        final List<AddressDTO> addressDTOList = addressList.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class)).toList();
        return addressDTOList;
    }
    public List<AddressDTO> getUserAddresses(Users users) {
        List<Address> addresses = users.getAddresses();
        return addresses.stream()
                .map(address -> modelMapper.map(address, AddressDTO.class))
                .toList();
    }
    @Override
    public AddressDTO updateAddressById(Long addressId, AddressDTO addressDTO){
        final Address addressFromDB = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        addressFromDB.setCountry(addressDTO.getCountry());
        addressFromDB.setCity(addressDTO.getCity());
        addressFromDB.setState(addressDTO.getState());
        addressFromDB.setStreet(addressDTO.getStreet());
        addressFromDB.setPinCode(addressDTO.getPinCode());
        addressFromDB.setBuildingName(addressDTO.getBuildingName());
        final Address updatedAddress = addressRepository.save(addressFromDB);
        final AddressDTO addressDTO1 = modelMapper.map(updatedAddress, AddressDTO.class);

        final Users users = addressFromDB.getUsers();
        users.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        users.getAddresses().add(updatedAddress);
        userRepository.save(users);
        return addressDTO1;
    }
    @Override
    public AddressDTO partialUpdateAddressById(Long addressId, AddressDTO addressDTO){
        final Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));
        if(addressDTO.getCountry()!=null) {
            address.setCountry(addressDTO.getCountry());
        }
        if(addressDTO.getCity()!=null) {
            address.setCity(addressDTO.getCity());
        }
        if(addressDTO.getState()!=null) {
            address.setState(addressDTO.getState());
        }
        if(addressDTO.getStreet()!=null) {
            address.setStreet(addressDTO.getStreet());
        }
        if(addressDTO.getPinCode()!=null) {
            address.setPinCode(addressDTO.getPinCode());
        }
        if(addressDTO.getBuildingName()!=null) {
            address.setBuildingName(addressDTO.getBuildingName());
        }
        final Address updateAddress = addressRepository.save(address);
        final AddressDTO addressDTO1 = modelMapper.map(updateAddress, AddressDTO.class);
        return addressDTO1;
    }
    @Override
    public String deleteAddress(Long addressId){
        Address addressFromDB = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        final Users users = addressFromDB.getUsers();
        users.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        userRepository.save(users);
        addressRepository.delete(addressFromDB);
        return "Address deleted successfully with addressId: " + addressId;
    }
}
