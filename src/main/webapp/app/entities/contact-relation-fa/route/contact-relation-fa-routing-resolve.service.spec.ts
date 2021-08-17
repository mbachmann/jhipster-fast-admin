jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IContactRelationFa, ContactRelationFa } from '../contact-relation-fa.model';
import { ContactRelationFaService } from '../service/contact-relation-fa.service';

import { ContactRelationFaRoutingResolveService } from './contact-relation-fa-routing-resolve.service';

describe('Service Tests', () => {
  describe('ContactRelationFa routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ContactRelationFaRoutingResolveService;
    let service: ContactRelationFaService;
    let resultContactRelationFa: IContactRelationFa | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ContactRelationFaRoutingResolveService);
      service = TestBed.inject(ContactRelationFaService);
      resultContactRelationFa = undefined;
    });

    describe('resolve', () => {
      it('should return IContactRelationFa returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactRelationFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactRelationFa).toEqual({ id: 123 });
      });

      it('should return new IContactRelationFa if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactRelationFa = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultContactRelationFa).toEqual(new ContactRelationFa());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ContactRelationFa })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultContactRelationFa = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultContactRelationFa).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
