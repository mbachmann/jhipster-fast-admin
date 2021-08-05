import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContactPersonMySuffix } from '../contact-person-my-suffix.model';

@Component({
  selector: 'jhl-contact-person-my-suffix-detail',
  templateUrl: './contact-person-my-suffix-detail.component.html',
})
export class ContactPersonMySuffixDetailComponent implements OnInit {
  contactPerson: IContactPersonMySuffix | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactPerson }) => {
      this.contactPerson = contactPerson;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
