jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IContactGroup, ContactGroup } from '../contact-group.model';
import { ContactGroupService } from '../service/contact-group.service';

import { ContactGroupRoutingResolveService } from './contact-group-routing-resolve.service';

describe('Service Tests', () => {
  describe('ContactGroup routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ContactGroupRoutingResolveService;
    let service: ContactGroupService;
    let resultContactGroup: IContactGroup | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ContactGroupRoutingResolveService);
      service = TestBed.inject(ContactGroupService);
      resultContactGroup = undefined;
    });

    describe('resolve', () => {
      it('should return IContactGroup returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactGroup = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactGroup).toEqual({ id: 123 });
      });

      it('should return new IContactGroup if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactGroup = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultContactGroup).toEqual(new ContactGroup());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ContactGroup })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactGroup = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactGroup).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
