import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProjectFa, ProjectFa } from '../project-fa.model';
import { ProjectFaService } from '../service/project-fa.service';

@Injectable({ providedIn: 'root' })
export class ProjectFaRoutingResolveService implements Resolve<IProjectFa> {
  constructor(protected service: ProjectFaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProjectFa> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((project: HttpResponse<ProjectFa>) => {
          if (project.body) {
            return of(project.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ProjectFa());
  }
}
