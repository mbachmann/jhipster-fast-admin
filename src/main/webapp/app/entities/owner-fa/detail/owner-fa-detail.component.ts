import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOwnerFa } from '../owner-fa.model';

@Component({
  selector: 'fa-owner-fa-detail',
  templateUrl: './owner-fa-detail.component.html',
})
export class OwnerFaDetailComponent implements OnInit {
  owner: IOwnerFa | null = null;

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
