import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContactGroupMySuffix } from '../contact-group-my-suffix.model';

@Component({
  selector: 'jhl-contact-group-my-suffix-detail',
  templateUrl: './contact-group-my-suffix-detail.component.html',
})
export class ContactGroupMySuffixDetailComponent implements OnInit {
  contactGroup: IContactGroupMySuffix | null = null;

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
