import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContactAddressMySuffix } from '../contact-address-my-suffix.model';

@Component({
  selector: 'jhl-contact-address-my-suffix-detail',
  templateUrl: './contact-address-my-suffix-detail.component.html',
})
export class ContactAddressMySuffixDetailComponent implements OnInit {
  contactAddress: IContactAddressMySuffix | null = null;

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
