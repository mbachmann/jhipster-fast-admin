import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProjectFa } from '../project-fa.model';

@Component({
  selector: 'fa-project-fa-detail',
  templateUrl: './project-fa-detail.component.html',
})
export class ProjectFaDetailComponent implements OnInit {
  project: IProjectFa | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ project }) => {
      this.project = project;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
