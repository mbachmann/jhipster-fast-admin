import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPermissionFa } from '../permission-fa.model';

@Component({
  selector: 'fa-permission-fa-detail',
  templateUrl: './permission-fa-detail.component.html',
})
export class PermissionFaDetailComponent implements OnInit {
  permission: IPermissionFa | null = null;

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
