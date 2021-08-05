import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContactRelationMySuffix } from '../contact-relation-my-suffix.model';

@Component({
  selector: 'jhl-contact-relation-my-suffix-detail',
  templateUrl: './contact-relation-my-suffix-detail.component.html',
})
export class ContactRelationMySuffixDetailComponent implements OnInit {
  contactRelation: IContactRelationMySuffix | null = null;

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
