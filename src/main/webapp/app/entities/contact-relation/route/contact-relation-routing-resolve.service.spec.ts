jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IContactRelation, ContactRelation } from '../contact-relation.model';
import { ContactRelationService } from '../service/contact-relation.service';

import { ContactRelationRoutingResolveService } from './contact-relation-routing-resolve.service';

describe('Service Tests', () => {
  describe('ContactRelation routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ContactRelationRoutingResolveService;
    let service: ContactRelationService;
    let resultContactRelation: IContactRelation | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ContactRelationRoutingResolveService);
      service = TestBed.inject(ContactRelationService);
      resultContactRelation = undefined;
    });

    describe('resolve', () => {
      it('should return IContactRelation returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactRelation = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactRelation).toEqual({ id: 123 });
      });

      it('should return new IContactRelation if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactRelation = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultContactRelation).toEqual(new ContactRelation());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ContactRelation })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactRelation = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactRelation).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
