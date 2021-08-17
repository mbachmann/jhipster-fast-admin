import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContactAddress } from '../contact-address.model';

@Component({
  selector: 'fa-contact-address-detail',
  templateUrl: './contact-address-detail.component.html',
})
export class ContactAddressDetailComponent implements OnInit {
  contactAddress: IContactAddress | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactAddress }) => {
      this.contactAddress = contactAddress;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
