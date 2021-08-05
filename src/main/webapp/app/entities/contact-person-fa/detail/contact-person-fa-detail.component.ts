import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContactPersonFa } from '../contact-person-fa.model';

@Component({
  selector: 'fa-contact-person-fa-detail',
  templateUrl: './contact-person-fa-detail.component.html',
})
export class ContactPersonFaDetailComponent implements OnInit {
  contactPerson: IContactPersonFa | null = null;

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
