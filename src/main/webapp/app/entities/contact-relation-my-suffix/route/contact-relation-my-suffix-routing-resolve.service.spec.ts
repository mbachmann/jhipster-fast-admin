jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IContactRelationMySuffix, ContactRelationMySuffix } from '../contact-relation-my-suffix.model';
import { ContactRelationMySuffixService } from '../service/contact-relation-my-suffix.service';

import { ContactRelationMySuffixRoutingResolveService } from './contact-relation-my-suffix-routing-resolve.service';

describe('Service Tests', () => {
  describe('ContactRelationMySuffix routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ContactRelationMySuffixRoutingResolveService;
    let service: ContactRelationMySuffixService;
    let resultContactRelationMySuffix: IContactRelationMySuffix | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ContactRelationMySuffixRoutingResolveService);
      service = TestBed.inject(ContactRelationMySuffixService);
      resultContactRelationMySuffix = undefined;
    });

    describe('resolve', () => {
      it('should return IContactRelationMySuffix returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactRelationMySuffix = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactRelationMySuffix).toEqual({ id: 123 });
      });

      it('should return new IContactRelationMySuffix if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactRelationMySuffix = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultContactRelationMySuffix).toEqual(new ContactRelationMySuffix());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ContactRelationMySuffix })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactRelationMySuffix = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactRelationMySuffix).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
