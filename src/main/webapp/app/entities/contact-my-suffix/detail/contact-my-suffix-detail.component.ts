import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContactMySuffix } from '../contact-my-suffix.model';

@Component({
  selector: 'jhl-contact-my-suffix-detail',
  templateUrl: './contact-my-suffix-detail.component.html',
})
export class ContactMySuffixDetailComponent implements OnInit {
  contact: IContactMySuffix | null = null;

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
