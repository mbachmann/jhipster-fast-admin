jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IProjectFa, ProjectFa } from '../project-fa.model';
import { ProjectFaService } from '../service/project-fa.service';

import { ProjectFaRoutingResolveService } from './project-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('ProjectFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ProjectFaRoutingResolveService;
    let service: ProjectFaService;
    let resultProjectFa: IProjectFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ProjectFaRoutingResolveService);
      service = TestBed.inject(ProjectFaService);
      resultProjectFa = undefined;
    });

    describe('resolve', () => {
      it('should return IProjectFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultProjectFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultProjectFa).toEqual({ id: 123 });
      });

      it('should return new IProjectFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultProjectFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultProjectFa).toEqual(new ProjectFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ProjectFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultProjectFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultProjectFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
