import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContactGroup } from '../contact-group.model';

@Component({
  selector: 'fa-contact-group-detail',
  templateUrl: './contact-group-detail.component.html',
})
export class ContactGroupDetailComponent implements OnInit {
  contactGroup: IContactGroup | null = null;

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
