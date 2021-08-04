jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CountryMySuffixService } from '../service/country-my-suffix.service';

import { CountryMySuffixComponent } from './country-my-suffix.component';

describe('Component Tests', () => {
  describe('CountryMySuffix Management Component', () => {
    let comp: CountryMySuffixComponent;
    let fixture: ComponentFixture<CountryMySuffixComponent>;
    let service: CountryMySuffixService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CountryMySuffixComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { snapshot: { queryParams: {} } },
          },
        ],
      })
        .overrideTemplate(CountryMySuffixComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CountryMySuffixComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CountryMySuffixService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.countries?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
