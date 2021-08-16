import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContactGroupFa } from '../contact-group-fa.model';

@Component({
  selector: 'fa-contact-group-fa-detail',
  templateUrl: './contact-group-fa-detail.component.html',
})
export class ContactGroupFaDetailComponent implements OnInit {
  contactGroup: IContactGroupFa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactGroup }) => {
      this.contactGroup = contactGroup;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
