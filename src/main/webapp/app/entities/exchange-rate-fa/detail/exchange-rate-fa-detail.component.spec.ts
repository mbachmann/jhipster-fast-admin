import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ExchangeRateFaDetailComponent } from './exchange-rate-fa-detail.component';

describe('Component Tests', () => {
  describe('ExchangeRateFa Management Detail Component', () => {
    let comp: ExchangeRateFaDetailComponent;
    let fixture: ComponentFixture<ExchangeRateFaDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ExchangeRateFaDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ exchangeRate: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ExchangeRateFaDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExchangeRateFaDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load exchangeRate on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.exchangeRate).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
