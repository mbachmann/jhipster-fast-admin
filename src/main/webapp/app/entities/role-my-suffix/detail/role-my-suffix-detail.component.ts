import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRoleMySuffix } from '../role-my-suffix.model';

@Component({
  selector: 'jhl-role-my-suffix-detail',
  templateUrl: './role-my-suffix-detail.component.html',
})
export class RoleMySuffixDetailComponent implements OnInit {
  role: IRoleMySuffix | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ role }) => {
      this.role = role;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
