import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IResourcePermission } from '../resource-permission.model';

@Component({
  selector: 'fa-resource-permission-detail',
  templateUrl: './resource-permission-detail.component.html',
})
export class ResourcePermissionDetailComponent implements OnInit {
  resourcePermission: IResourcePermission | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resourcePermission }) => {
      this.resourcePermission = resourcePermission;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
