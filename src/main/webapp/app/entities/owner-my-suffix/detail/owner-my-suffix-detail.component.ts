import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOwnerMySuffix } from '../owner-my-suffix.model';

@Component({
  selector: 'jhl-owner-my-suffix-detail',
  templateUrl: './owner-my-suffix-detail.component.html',
})
export class OwnerMySuffixDetailComponent implements OnInit {
  owner: IOwnerMySuffix | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ owner }) => {
      this.owner = owner;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
