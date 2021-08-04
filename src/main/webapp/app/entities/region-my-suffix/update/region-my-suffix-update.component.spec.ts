jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { RegionMySuffixService } from '../service/region-my-suffix.service';
import { IRegionMySuffix, RegionMySuffix } from '../region-my-suffix.model';
import { ICountryMySuffix } from 'app/entities/country-my-suffix/country-my-suffix.model';
import { CountryMySuffixService } from 'app/entities/country-my-suffix/service/country-my-suffix.service';

import { RegionMySuffixUpdateComponent } from './region-my-suffix-update.component';

describe('Component Tests', () => {
  describe('RegionMySuffix Management Update Component', () => {
    let comp: RegionMySuffixUpdateComponent;
    let fixture: ComponentFixture<RegionMySuffixUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let regionService: RegionMySuffixService;
    let countryService: CountryMySuffixService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [RegionMySuffixUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(RegionMySuffixUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(RegionMySuffixUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      regionService = TestBed.inject(RegionMySuffixService);
      countryService = TestBed.inject(CountryMySuffixService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call CountryMySuffix query and add missing value', () => {
        const region: IRegionMySuffix = { id: 456 };
        const country: ICountryMySuffix = { id: 47433 };
        region.country = country;

        const countryCollection: ICountryMySuffix[] = [{ id: 46183 }];
        jest.spyOn(countryService, 'query').mockReturnValue(of(new HttpResponse({ body: countryCollection })));
        const additionalCountryMySuffixes = [country];
        const expectedCollection: ICountryMySuffix[] = [...additionalCountryMySuffixes, ...countryCollection];
        jest.spyOn(countryService, 'addCountryMySuffixToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ region });
        comp.ngOnInit();

        expect(countryService.query).toHaveBeenCalled();
        expect(countryService.addCountryMySuffixToCollectionIfMissing).toHaveBeenCalledWith(
          countryCollection,
          ...additionalCountryMySuffixes
        );
        expect(comp.countriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const region: IRegionMySuffix = { id: 456 };
        const country: ICountryMySuffix = { id: 90627 };
        region.country = country;

        activatedRoute.data = of({ region });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(region));
        expect(comp.countriesSharedCollection).toContain(country);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<RegionMySuffix>>();
        const region = { id: 123 };
        jest.spyOn(regionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ region });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: region }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(regionService.update).toHaveBeenCalledWith(region);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<RegionMySuffix>>();
        const region = new RegionMySuffix();
        jest.spyOn(regionService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ region });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: region }));
        saveSubject.complete();

        // THEN
        expect(regionService.create).toHaveBeenCalledWith(region);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<RegionMySuffix>>();
        const region = { id: 123 };
        jest.spyOn(regionService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ region });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(regionService.update).toHaveBeenCalledWith(region);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCountryMySuffixById', () => {
        it('Should return tracked CountryMySuffix primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCountryMySuffixById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
