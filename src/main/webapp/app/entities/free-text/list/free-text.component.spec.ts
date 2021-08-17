import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { FreeTextService } from '../service/free-text.service';

import { FreeTextComponent } from './free-text.component';

describe('Component Tests', () => {
  describe('FreeText Management Component', () => {
    let comp: FreeTextComponent;
    let fixture: ComponentFixture<FreeTextComponent>;
    let service: FreeTextService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [FreeTextComponent],
      })
        .overrideTemplate(FreeTextComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FreeTextComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(FreeTextService);

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
