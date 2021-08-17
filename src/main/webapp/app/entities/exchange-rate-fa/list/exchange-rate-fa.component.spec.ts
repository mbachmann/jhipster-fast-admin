import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ExchangeRateFaService } from '../service/exchange-rate-fa.service';

import { ExchangeRateFaComponent } from './exchange-rate-fa.component';

describe('Component Tests', () => {
  describe('ExchangeRateFa Management Component', () => {
    let comp: ExchangeRateFaComponent;
    let fixture: ComponentFixture<ExchangeRateFaComponent>;
    let service: ExchangeRateFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExchangeRateFaComponent],
      })
        .overrideTemplate(ExchangeRateFaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExchangeRateFaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ExchangeRateFaService);

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
      expect(comp.exchangeRates?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
