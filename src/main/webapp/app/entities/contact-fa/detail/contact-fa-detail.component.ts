import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContactFa } from '../contact-fa.model';

@Component({
  selector: 'fa-contact-fa-detail',
  templateUrl: './contact-fa-detail.component.html',
})
export class ContactFaDetailComponent implements OnInit {
  contact: IContactFa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contact }) => {
      this.contact = contact;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
