import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPermissionMySuffix } from '../permission-my-suffix.model';

@Component({
  selector: 'jhl-permission-my-suffix-detail',
  templateUrl: './permission-my-suffix-detail.component.html',
})
export class PermissionMySuffixDetailComponent implements OnInit {
  permission: IPermissionMySuffix | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ permission }) => {
      this.permission = permission;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
