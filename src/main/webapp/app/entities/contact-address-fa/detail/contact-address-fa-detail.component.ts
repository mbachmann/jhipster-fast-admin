import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContactAddressFa } from '../contact-address-fa.model';

@Component({
  selector: 'fa-contact-address-fa-detail',
  templateUrl: './contact-address-fa-detail.component.html',
})
export class ContactAddressFaDetailComponent implements OnInit {
  contactAddress: IContactAddressFa | null = null;

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
