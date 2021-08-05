import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IRoleFa } from '../role-fa.model';

@Component({
  selector: 'fa-role-fa-detail',
  templateUrl: './role-fa-detail.component.html',
})
export class RoleFaDetailComponent implements OnInit {
  role: IRoleFa | null = null;

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
