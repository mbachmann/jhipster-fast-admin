jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CountryMySuffixService } from '../service/country-my-suffix.service';
import { ICountryMySuffix, CountryMySuffix } from '../country-my-suffix.model';
import { ILocationMySuffix } from 'app/entities/location-my-suffix/location-my-suffix.model';
import { LocationMySuffixService } from 'app/entities/location-my-suffix/service/location-my-suffix.service';

import { CountryMySuffixUpdateComponent } from './country-my-suffix-update.component';

describe('Component Tests', () => {
  describe('CountryMySuffix Management Update Component', () => {
    let comp: CountryMySuffixUpdateComponent;
    let fixture: ComponentFixture<CountryMySuffixUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let countryService: CountryMySuffixService;
    let locationService: LocationMySuffixService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CountryMySuffixUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CountryMySuffixUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CountryMySuffixUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      countryService = TestBed.inject(CountryMySuffixService);
      locationService = TestBed.inject(LocationMySuffixService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call LocationMySuffix query and add missing value', () => {
        const country: ICountryMySuffix = { id: 456 };
        const location: ILocationMySuffix = { id: 70520 };
        country.location = location;

        const locationCollection: ILocationMySuffix[] = [{ id: 89023 }];
        jest.spyOn(locationService, 'query').mockReturnValue(of(new HttpResponse({ body: locationCollection })));
        const additionalLocationMySuffixes = [location];
        const expectedCollection: ILocationMySuffix[] = [...additionalLocationMySuffixes, ...locationCollection];
        jest.spyOn(locationService, 'addLocationMySuffixToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ country });
        comp.ngOnInit();

        expect(locationService.query).toHaveBeenCalled();
        expect(locationService.addLocationMySuffixToCollectionIfMissing).toHaveBeenCalledWith(
          locationCollection,
          ...additionalLocationMySuffixes
        );
        expect(comp.locationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const country: ICountryMySuffix = { id: 456 };
        const location: ILocationMySuffix = { id: 95181 };
        country.location = location;

        activatedRoute.data = of({ country });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(country));
        expect(comp.locationsSharedCollection).toContain(location);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CountryMySuffix>>();
        const country = { id: 123 };
        jest.spyOn(countryService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ country });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: country }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(countryService.update).toHaveBeenCalledWith(country);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CountryMySuffix>>();
        const country = new CountryMySuffix();
        jest.spyOn(countryService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ country });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: country }));
        saveSubject.complete();

        // THEN
        expect(countryService.create).toHaveBeenCalledWith(country);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<CountryMySuffix>>();
        const country = { id: 123 };
        jest.spyOn(countryService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ country });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(countryService.update).toHaveBeenCalledWith(country);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackLocationMySuffixById', () => {
        it('Should return tracked LocationMySuffix primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackLocationMySuffixById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
