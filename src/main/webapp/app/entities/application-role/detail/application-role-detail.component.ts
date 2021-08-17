import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IApplicationRole } from '../application-role.model';

@Component({
  selector: 'fa-application-role-detail',
  templateUrl: './application-role-detail.component.html',
})
export class ApplicationRoleDetailComponent implements OnInit {
  applicationRole: IApplicationRole | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ applicationRole }) => {
      this.applicationRole = applicationRole;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
