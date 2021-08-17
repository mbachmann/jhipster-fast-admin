import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContactRelation } from '../contact-relation.model';

@Component({
  selector: 'fa-contact-relation-detail',
  templateUrl: './contact-relation-detail.component.html',
})
export class ContactRelationDetailComponent implements OnInit {
  contactRelation: IContactRelation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contactRelation }) => {
      this.contactRelation = contactRelation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
