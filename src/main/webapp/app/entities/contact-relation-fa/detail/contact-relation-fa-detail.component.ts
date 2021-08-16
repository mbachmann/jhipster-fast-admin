import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContactRelationFa } from '../contact-relation-fa.model';

@Component({
  selector: 'fa-contact-relation-fa-detail',
  templateUrl: './contact-relation-fa-detail.component.html',
})
export class ContactRelationFaDetailComponent implements OnInit {
  contactRelation: IContactRelationFa | null = null;

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
