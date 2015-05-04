package com.movie.watch.busevents;

import com.movie.watch.model.address.Address;

public class GoogleAddressFetchEvent {
    private Address addressResult;

    public GoogleAddressFetchEvent(Address addressResult) {
      this.addressResult = addressResult;
    }

    public Address getAddresses() {
      return addressResult;
    }

}
