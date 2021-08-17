import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { FreeTextFaService } from '../service/free-text-fa.service';

import { FreeTextFaComponent } from './free-text-fa.component';

describe('Component Tests', () => {
  describe('FreeTextFa Management Component', () => {
    let comp: FreeTextFaComponent;
    let fixture: ComponentFixture<FreeTextFaComponent>;
    let service: FreeTextFaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FreeTextFaComponent],
      })
        .overrideTemplate(FreeTextFaComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FreeTextFaComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(FreeTextFaService);

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
      expect(comp.freeTexts?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
